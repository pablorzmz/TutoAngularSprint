import {Component, Input, OnInit, OnChanges, SimpleChanges} from '@angular/core';

@Component({
  selector: 'app-paginator-nav',
  templateUrl: './paginator.component.html'
})
export class PaginatorComponent implements OnInit, OnChanges {

  @ Input() paginador: any;
  paginas: number[];
  desde: number;
  hasta: number;

  constructor() { }

  // Cuando cambia el atributo inyectado por el para de paginador, actualiza y recalcula rangos

  ngOnChanges( changes: SimpleChanges ) {
    const paginadorActualizado = changes.paginador;
    // Se invoca solamente si hay un cambio anterior en este objeto paginador
    if (paginadorActualizado.previousValue) {
      this.initPaginator();
    }
  }

  ngOnInit() {
    this.initPaginator();
  }

  private initPaginator(): void {
    this.desde = Math.min( Math.max(1, this.paginador.number - 4 ), this.paginador.totalPages - 5 );
    this.hasta = Math.max( Math.min( this.paginador.totalPages, this.paginador.number + 4), 6 );

    if (this.paginador.totalPages > 5) {
      this.paginas = new Array((this.hasta - this.desde) + 1).fill(0).map(
        (valor, indice) => indice + this.desde
      );
    } else {
      this.paginas = new Array(this.paginador.totalPages).fill(0).map(
        (valor, indice) => indice + 1
      );
    }
  }
}
