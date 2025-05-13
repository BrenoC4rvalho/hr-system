import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from "../../shared/navbar/navbar.component";
import { Eye, LucideAngularModule, Pencil } from 'lucide-angular';
import { Position } from '../../core/model/position';
import { PositionService } from '../../core/service/position.service';
import { CommonModule } from '@angular/common';
import { NewPositionModalComponent } from "../../components/new-position-modal/new-position-modal.component";
import { ErrorModalComponent } from "../../components/error-modal/error-modal";
import { PositionProfileModalComponent } from "../../components/position-profile-modal/position-profile-modal.component";

@Component({
  selector: 'app-positions',
  imports: [NavbarComponent, LucideAngularModule, CommonModule, NewPositionModalComponent, ErrorModalComponent, PositionProfileModalComponent],
  templateUrl: './positions.component.html',
})
export class PositionsComponent implements OnInit {

  readonly EyeIcon = Eye;
  readonly PencilIcon = Pencil;

  isModalNewPositionOpen: boolean = false;

  showErrorModal: boolean = false;
  errorMessage: string = '';

  positions: Position[] = [];

  selectedPositionForModal: Position | null = null;
  showPositionProfileModal: boolean = false;

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

  openModalNewPosition() {
    this.isModalNewPositionOpen = true;
  }

  closeModalNewPosition() {
    this.isModalNewPositionOpen = false;
  }

  onPositionCreated($event: Position) {
    this.positions.push($event);
  }

  handleErrorModal($event: string) {
    this.showErrorModal = true;
    this.errorMessage = $event;
  }

  openPositionProfileModal(selectedPosition: Position): void {
      this.selectedPositionForModal = selectedPosition;
      this.showPositionProfileModal = true;
  }

  closePositionProfileModal(): void {
    this.showPositionProfileModal = false;
    this.selectedPositionForModal = null;
  }

}
