import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from "../../shared/navbar/navbar.component";
import { Eye, LucideAngularModule, Pencil } from 'lucide-angular';
import { Position } from '../../core/model/position';
import { PositionService } from '../../core/service/position.service';
import { CommonModule } from '@angular/common';
import { NewPositionModalComponent } from "../../components/new-position-modal/new-position-modal.component";
import { ErrorModalComponent } from "../../components/error-modal/error-modal";
import { PositionProfileModalComponent } from "../../components/position-profile-modal/position-profile-modal.component";
import { EditPositionModalComponent } from "../../components/edit-position-modal/edit-position-modal.component";

@Component({
  selector: 'app-positions',
  imports: [NavbarComponent, LucideAngularModule, CommonModule, NewPositionModalComponent, ErrorModalComponent, PositionProfileModalComponent, EditPositionModalComponent],
  templateUrl: './positions.component.html',
})
export class PositionsComponent implements OnInit {

  readonly EyeIcon = Eye;
  readonly PencilIcon = Pencil;

  isModalNewPositionOpen: boolean = false;

  showErrorModal: boolean = false;
  errorMessage: string = '';

  positions: Position[] = [];

  selectedPositionForModal: Position | undefined;
  showPositionProfileModal: boolean = false;
  isModalEditPositionOpen: boolean = false;

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

  openPositionEditModal(selectedPosition: Position): void {
    this.selectedPositionForModal = selectedPosition;
    this.isModalEditPositionOpen = true;
  }

  closePositionProfileModal(): void {
    this.showPositionProfileModal = false;
    this.selectedPositionForModal = undefined;
  }

  closePositionEditModal(): void {
    this.isModalEditPositionOpen = false;
    this.selectedPositionForModal = undefined;
  }

  handleEditPosition(updated: Position): void {
    const index = this.positions.findIndex(p => p.id === updated.id);
    if (index !== -1) {
      this.positions[index] = updated;
    }
  }

}
