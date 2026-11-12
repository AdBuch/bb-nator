import { Component, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LEARNING_PERSPECTIVES } from './core/learning-perspectives';

type View = 'mentor' | 'learner';
interface SystemStatus { service: string; status: string; version: string; }

@Component({
  selector: 'app-root',
  imports: [],
  templateUrl: './app.html'
})
export class App {
  private readonly http = inject(HttpClient);

  readonly activeView = signal<View>('mentor');
  readonly apiState = signal('Verbindung wird geprüft …');
  readonly perspectives = LEARNING_PERSPECTIVES;

  constructor() {
    this.http.get<SystemStatus>('/api/v1/system/status').subscribe({
      next: (result) => this.apiState.set(result.status === 'READY' ? 'API erreichbar · v' + result.version : 'API meldet ' + result.status),
      error: () => this.apiState.set('API nicht erreichbar · Backend starten')
    });
  }

  selectView(view: View): void {
    this.activeView.set(view);
  }
}
