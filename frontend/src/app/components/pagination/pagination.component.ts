import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ChevronFirst, ChevronLast, ChevronLeft, ChevronRight, LucideAngularModule } from 'lucide-angular';

@Component({
  selector: 'app-pagination',
  imports: [LucideAngularModule],
  templateUrl: './pagination.component.html',
})
export class PaginationComponent {


  readonly ChevronFirstIcon = ChevronFirst;
  readonly ChevronLeftIcon = ChevronLeft;
  readonly ChevronRightIcon = ChevronRight;
  readonly ChevronLastIcon = ChevronLast;

  @Input() currentPage!: number;
  @Input() totalPages!: number;
  @Output() pageChanged = new EventEmitter<number>();

  firstPage(): void {
    this.pageChanged.emit(0);
  }

  previousPage(): void {
    if(this.currentPage > 0) {
      this.pageChanged.emit(this.currentPage - 1);
    }
  }

  nextPage(): void {
    if(this.currentPage < this.totalPages - 1) {
      this.pageChanged.emit(this.currentPage + 1);
    }
  }

  lastPage(): void {
    this.pageChanged.emit(this.totalPages - 1);
  }


}
