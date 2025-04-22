export type Field =
  | {
      label: string;
      name: string;
      type: 'input';
      inputType: string;
      placeholder: string;
    }
  | {
      label: string;
      name: string;
      type: 'select';
      placeholder: string;
      options: any[]; // ou Gender[] | Department[] | Position[] etc.
    };
