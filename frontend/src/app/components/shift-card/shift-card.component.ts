import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-shift-card',
  imports: [CommonModule],
  templateUrl: './shift-card.component.html',
})
export class ShiftCardComponent {

  totalOfEmployees: number = 100
  employeesN: number = 30
  employeesM: number = 20
  employeesA: number = 50

  primary = '#0077B6';
  deep = '#1E1E1E';
  yellow = '#f59e0b'

  get conicGradientTailwind(): string {
    const morningPercent = (this.employeesM / this.totalOfEmployees) * 100;
    const afternoonPercent = (this.employeesA / this.totalOfEmployees) * 100;
    const nightPercent = (this.employeesN / this.totalOfEmployees) * 100;

    const morningDeg = (morningPercent * 360) / 100;
    const afternoonDeg = (afternoonPercent * 360) / 100;
    const nightDeg = (nightPercent * 360) / 100;

    return `
      background-image: conic-gradient(
        ${this.yellow} 0deg ${morningDeg}deg,
        ${this.primary} ${morningDeg}deg ${morningDeg + afternoonDeg}deg,
        ${this.deep} ${morningDeg + afternoonDeg}deg 360deg
      );
    `;
  }
}
