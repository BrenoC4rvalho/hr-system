import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from "../../shared/navbar/navbar.component";
import { Eye, LucideAngularModule } from 'lucide-angular';
import { Position } from '../../core/model/position';
import { PositionService } from '../../core/service/position.service';
import { PaginatedUsersResponse } from '../../core/model/paginated-users-response';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-positions',
  imports: [NavbarComponent, LucideAngularModule, CommonModule],
  templateUrl: './positions.component.html',
})
export class PositionsComponent implements OnInit {

  readonly EyeIcon = Eye;

  showErrorModal: boolean = false;
  errorMessage: string = '';

  positions: Position[] = [];

  constructor(private positionService: PositionService) {}

  ngOnInit(): void {
    this.getPositions();
  }

  getPositions(): void {
    this.positionService.getAll().subscribe({
      next: (response: Position[]) => {
        this.positions = response;
      },
      error: (error) => {
        if(error && error.error) {
          this.showErrorModal = true;
          this.errorMessage = error.error;
        } else {
          this.showErrorModal = true;
          this.errorMessage = 'An unexpected error occurred. Please try again later.';
        }
      }
    })
  }

}
