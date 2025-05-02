import { CommonModule } from '@angular/common';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ArrowLeft, ArrowRight, Cake, LucideAngularModule, UserSearch } from 'lucide-angular';
import { EmployeeBirthday } from '../../core/model/employee-birthday';
import { EmployeeService } from '../../core/service/employee.service';
import { BirthdaysResponse } from '../../core/model/birthday-response';

@Component({
  selector: 'app-list-birthday',
  imports: [FormsModule, CommonModule, LucideAngularModule],
  templateUrl: './list-birthday.component.html',
})
export class ListBirthdayComponent implements OnInit {

  readonly UserRoundSearchIcon = UserSearch;
  readonly ArrowLeftIcon = ArrowLeft;
  readonly ArrowRightIcon = ArrowRight;
  readonly CakeIcon = Cake;

  @Output() errorMessage = new EventEmitter<string>();

  searchText: string = '';
  today: Date = new Date();

  // JavaScript's getMonth() returns a zero-based month (0 = January, 11 = December),
  // so we add 1 to match the API expectation (1 = January, 12 = December)
  monthNumber: number = this.today.getMonth() + 1;
  monthName: string = '';
  totalEmployees: number = 0;
  employees: EmployeeBirthday[] = [];

  constructor(private employeeService: EmployeeService) {}

  ngOnInit(): void {
      this.getEmployees(this.monthNumber);
  }

  getEmployees(month: number): void {
    this.employeeService.getEmployeesByBirthMonth(month).subscribe({
      next: (response: BirthdaysResponse) => {
        console.log("response", response)
        this.monthNumber = response.monthNumber;
        this.monthName = response.month;
        this.totalEmployees = response.totalEmployees;
        this.employees = response.employees;
      },
      error: (error) => {
        console.log("error");
        if(error && error.error) {
          this.errorMessage.emit(error.error);
        } else {
          this.errorMessage.emit('An unexpected error occurred. Please try again later.');
        }
      }
    })
  }

  nextMonth(): void {
    if(this.monthNumber == 12) {
      this.monthNumber = 1;
    } else {
      this.monthNumber += 1;
    }
    this.getEmployees(this.monthNumber);
  }

  previousMonth(): void {
    if(this.monthNumber == 1) {
      this.monthNumber = 12;
    } else {
      this.monthNumber -= 1;
    }
    this.getEmployees(this.monthNumber);
  }

}
