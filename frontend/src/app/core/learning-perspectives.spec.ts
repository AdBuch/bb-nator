import { describe, expect, it } from 'vitest';
import { LEARNING_PERSPECTIVES, hasAllSevenPerspectives } from './learning-perspectives';

describe('learning perspectives', () => {
  it('contains the seven product perspectives', () => {
    expect(LEARNING_PERSPECTIVES).toHaveLength(7);
    expect(hasAllSevenPerspectives(LEARNING_PERSPECTIVES)).toBe(true);
  });
  it('rejects an incomplete plan', () => {
    expect(hasAllSevenPerspectives(['Betrieb'])).toBe(false);
  });
});
