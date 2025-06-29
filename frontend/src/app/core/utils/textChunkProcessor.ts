export class TextChunkProcessor {
  private lastChar: string = '';

  public process(chunk: string): string {
    if (!chunk) {
      return '';
    }

    let processedChunk = chunk;

    if (this.needsSpace(this.lastChar, chunk[0])) {
      processedChunk = ' ' + chunk;
    }

    this.lastChar = chunk.charAt(chunk.length - 1);

    return processedChunk;
  }

  public reset(): void {
    this.lastChar = '';
  }

  private needsSpace(prevChar: string, currentChar: string): boolean {
    if (!prevChar) {
      return false;
    }

    const isLetter = (char: string) => /[a-zA-Z\u00C0-\u017F]/.test(char);

    const isPunctuation = (char: string) => /[.,!?;:)]/.test(char);

    return isLetter(prevChar) && isLetter(currentChar) && !isPunctuation(currentChar);
  }
}
