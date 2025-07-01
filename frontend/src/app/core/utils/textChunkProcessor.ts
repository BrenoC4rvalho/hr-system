export class TextChunkProcessor {
  private isFirstChunk: boolean = true;

  /**
   * Processa um novo pedaço de texto. Adiciona um espaço antes do pedaço,
   * a menos que seja o primeiro ou comece com pontuação.
   * @param chunk O novo pedaço de texto recebido.
   * @returns O chunk processado, com um espaço se necessário.
   */
  public process(chunk: string): string {
    if (!chunk) {
      return '';
    }

    // Lista de sinais de pontuação que não devem ter um espaço antes.
    const punctuation = '.,!?;:)';

    // Se for o primeiro pedaço ou se o pedaço começar com pontuação,
    // não adicione um espaço antes.
    if (this.isFirstChunk || punctuation.includes(chunk.charAt(0))) {
      this.isFirstChunk = false;
      return chunk;
    }

    // Para todos os outros pedaços, adicione um espaço antes.
    return ' ' + chunk;
  }

  /**
   * Reseta o processador para a próxima mensagem.
   */
  public reset(): void {
    this.isFirstChunk = true;
  }
}
