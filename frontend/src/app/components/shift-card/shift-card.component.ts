import { CommonModule } from '@angular/common';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { EmployeeService } from '../../core/service/employee.service';
import { EmployeeShiftSummary } from '../../core/model/employee-shift-summary';

@Component({
  selector: 'app-shift-card',
  imports: [CommonModule],
  templateUrl: './shift-card.component.html',
})
export class ShiftCardComponent implements OnInit {

  @Output() errorMessage = new EventEmitter<string>();

  totalOfEmployees: number = 0;
  employeesNight: number = 0;
  employeesMorning: number = 0;
  employeesAfternoon: number = 0;

  primary = '#0077B6';
  deep = '#1E1E1E';
  yellow = '#f59e0b'

  constructor(private employeeService: EmployeeService) {}

  ngOnInit(): void {
    this.getShiftSummary();
  }

  getShiftSummary(): void {
    this.employeeService.getEmployeeShiftSummary().subscribe({
      next: (response: EmployeeShiftSummary) => {
        this.employeesMorning = response.MORNING;
        this.employeesAfternoon = response.AFTERNOON;
        this.employeesNight = response.NIGHT;
        this.totalOfEmployees = this.employeesMorning + this.employeesAfternoon + this.employeesNight;
      },
      error: (error) => {
        const errorMsg = error?.error || 'An unexpected error occurred while fetching shift data.';
        this.errorMessage.emit(errorMsg);
      }
    });
  }

  get conicGradientTailwind(): string {
    if (this.totalOfEmployees === 0) {
      return `background-image: conic-gradient(#CED4DA 0deg 360deg);`;
    }

    const morningPercent = (this.employeesMorning / this.totalOfEmployees) * 100;
    const afternoonPercent = (this.employeesAfternoon / this.totalOfEmployees) * 100;

    const morningDeg = (morningPercent * 360) / 100;
    const afternoonDeg = (afternoonPercent * 360) / 100;

    return `
      background-image: conic-gradient(
        ${this.yellow} 0deg ${morningDeg}deg,
        ${this.primary} ${morningDeg}deg ${morningDeg + afternoonDeg}deg,
        ${this.deep} ${morningDeg + afternoonDeg}deg 360deg
      );
    `;
  }

}
