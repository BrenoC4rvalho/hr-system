import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-list-employees',
  imports: [CommonModule],
  templateUrl: './list-employees.component.html',
})
export class ListEmployeesComponent {
  employees = [
    { id: 1, name: 'John Fransico', phone: '1234567890', role: 'Developer', department: 'IT', shift: 'Morning' },
    { id: 1, name: 'John Neymar', phone: '1234567890', role: 'Developer', department: 'IT', shift: 'Afternoon' },
    { id: 1, name: 'John Rubens', phone: '1234567890', role: 'Developer', department: 'IT', shift: 'Night' },
    { id: 1, name: 'John Caetan', phone: '1234567890', role: 'Developer', department: 'IT', shift: 'Morning' }
  ]
}
