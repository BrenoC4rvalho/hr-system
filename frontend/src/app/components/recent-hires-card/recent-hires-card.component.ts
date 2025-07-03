import { Component, EventEmitter, Output } from '@angular/core';
import { EmployeeBasic } from '../../core/model/employee-basic';
import { EmployeeService } from '../../core/service/employee.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-recent-hires-card',
  imports: [CommonModule],
  templateUrl: './recent-hires-card.component.html',
})
export class RecentHiresCardComponent {

  @Output() errorMessage = new EventEmitter<string>();
  recentHires: EmployeeBasic[] = [];

  constructor(private employeeService: EmployeeService) {}

  ngOnInit(): void {
    this.employeeService.getRecentHires(90).subscribe({
      next: (response) => {
        this.recentHires = response;
      },
      error: (err) => this.errorMessage.emit('Could not load recent hires.'),
    });
  }

}
