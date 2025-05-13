export type Field =
  | {
      label: string;
      name: string;
      type: 'input';
      inputType: string;
      placeholder?: string;
      value?: string | number | Date
    }
  | {
      label: string;
      name: string;
      type: 'select';
      placeholder: string;
      options: any[];
    };
