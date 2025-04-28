import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LucideAngularModule, UserSearch } from 'lucide-angular';

@Component({
  selector: 'app-list-birthday',
  imports: [FormsModule, CommonModule, LucideAngularModule],
  templateUrl: './list-birthday.component.html',
})
export class ListBirthdayComponent {

  readonly UserRoundSearchIcon = UserSearch;

  searchText: string = '';
  today: Date = new Date();

  users = [
    { name: 'Edwards Mckenz', role: 'Chief Financial Officer (CFO)', avatar: 'https://via.placeholder.com/40' },
    { name: 'Daisy Wilson', role: 'Director of Finance', avatar: 'https://via.placeholder.com/40' },
    { name: 'Bianca Anderson', role: 'Financial Analyst', avatar: 'https://via.placeholder.com/40' },
    { name: 'Laurent Perrier', role: 'Finance Assistant', avatar: 'https://via.placeholder.com/40' },
    { name: 'Nunez Faulkner', role: 'Budget Analyst', avatar: 'https://via.placeholder.com/40' },
    { name: 'Gilbert Barrett', role: 'Financial Analyst', avatar: 'https://via.placeholder.com/40' }
  ];

  get filteredUsers() {
    return this.users.filter(u =>
      u.name.toLowerCase().includes(this.searchText.toLowerCase())
    );
  }

}
