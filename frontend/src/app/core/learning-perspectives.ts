/** Seven sources that contribute to one individual learning plan. */
export const LEARNING_PERSPECTIVES = [
  'Bildungsplan / BiVo',
  'Betrieb',
  'Berufsbildner-Vision',
  'Wünsche der Lernenden',
  'Betrieblicher Lehrpfad',
  'Überbetriebliche Kurse',
  'Berufsfachschule'
] as const;

export function hasAllSevenPerspectives(values: readonly string[]): boolean {
  return LEARNING_PERSPECTIVES.every((name) => values.includes(name));
}
