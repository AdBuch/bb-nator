import { Component, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';

type View = 'mentor' | 'learner' | 'admin';
type Progress = 'ASSIGNED' | 'IN_PROGRESS' | 'IN_REVIEW' | 'DONE';
type Evidence = 'MISSING' | 'SUBMITTED' | 'VERIFIED';
interface Learner { id: string; name: string; semester: number; }
interface Task { id: string; learnerId: string; title: string; learningGoal: string; status: Progress; evidence: Evidence; }
interface UpdateStatus { current: string; latest: string; updateAvailable: boolean; message: string; }

@Component({ selector: 'app-root', imports: [], templateUrl: './app.html' })
export class App {
  private readonly http = inject(HttpClient);
  readonly view = signal<View>('mentor');
  readonly learners = signal<Learner[]>([]);
  readonly tasks = signal<Task[]>([]);
  readonly demoEnabled = signal(false);
  readonly notice = signal('');
  readonly learnerId = signal('');
  readonly title = signal('');
  readonly goal = signal('Java & Testing');
  readonly updateToken = signal('');
  readonly updateState = signal<UpdateStatus | null>(null);
  readonly updateBusy = signal(false);
  readonly updateNotice = signal('Nur lokal mit scripts/start.sh oder scripts/start.ps1.');
  constructor() { this.reload(); }
  select(v: View): void { this.view.set(v); }
  reload(): void {
    this.http.get<Learner[]>('/api/v1/demo/learners').subscribe({
      next: x => { this.learners.set(x); this.demoEnabled.set(true); if(!this.learnerId() && x.length) this.learnerId.set(x[0].id); },
      error: () => { this.demoEnabled.set(false); this.notice.set('Demo ist nicht aktiviert. Bitte den lokalen Starter verwenden.'); }
    });
    this.http.get<Task[]>('/api/v1/demo/tasks').subscribe({next: x => this.tasks.set(x), error: () => this.tasks.set([])});
  }
  name(id: string): string { return this.learners().find(x => x.id === id)?.name || 'Unbekannt'; }
  count(id: string): number { return this.tasks().filter(x => x.learnerId === id).length; }
  totalDone(): number { return this.tasks().filter(x => x.status === 'DONE').length; }
  done(id: string): number { return this.tasks().filter(x => x.learnerId === id && x.status === 'DONE').length; }
  mine(): Task[] { return this.tasks().filter(x => x.learnerId === this.learnerId()); }
  create(event: Event): void {
    event.preventDefault();
    if (!this.title().trim() || !this.learnerId()) return;
    this.http.post('/api/v1/demo/tasks', { learnerId:this.learnerId(),title:this.title().trim(),learningGoal:this.goal() })
      .subscribe({next:()=>{this.notice.set('Aufgabe gespeichert.');this.title.set('');this.reload();},error:()=>this.notice.set('Speichern fehlgeschlagen.')});
  }
  progress(task: Task, status: Progress, evidence: Evidence): void {
    this.http.patch('/api/v1/demo/tasks/'+encodeURIComponent(task.id),{status,evidence})
      .subscribe({next:()=>{this.notice.set('Fortschritt gespeichert.');this.reload();},error:()=>this.notice.set('Statusänderung fehlgeschlagen.')});
  }
  private async updateApi(path: string, method='GET'): Promise<UpdateStatus> {
    const result=await fetch('http://127.0.0.1:8766'+path,{
      method,headers:{'X-Update-Token':this.updateToken()},cache:'no-store'
    });
    if(!result.ok) throw new Error('HTTP '+result.status);
    return await result.json() as UpdateStatus;
  }
  async check(): Promise<void> {
    this.updateBusy.set(true);
    try { const x=await this.updateApi('/check');this.updateState.set(x);this.updateNotice.set(x.message); }
    catch {this.updateNotice.set('Updater nicht erreichbar oder Admin-Token ungültig.');}
    finally {this.updateBusy.set(false);}
  }
  async apply(): Promise<void> {
    if(!this.updateState()?.updateAvailable || !window.confirm('Neue Version installieren und lokale Container neu starten?'))return;
    this.updateBusy.set(true);this.updateNotice.set('Installation läuft …');
    try {const x=await this.updateApi('/apply','POST');this.updateState.set(x);this.updateNotice.set(x.message);
      if (!x.updateAvailable && x.current===x.latest) window.location.reload();
    } catch {this.updateNotice.set('Installation nicht bestätigt. Terminal des Starters überprüfen.');}
    finally {this.updateBusy.set(false);}
  }
}
