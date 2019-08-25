import { Component, OnInit } from '@angular/core';
import {Cliente} from './cliente';
import {ClienteService} from './cliente.service';
import Swal from 'sweetalert2';
import {tap} from 'rxjs/operators';
import {ActivatedRoute} from '@angular/router';
import {PaginatorComponent} from '../paginator/paginator.component';
import {ModalService} from './detalle/modal.service';


@Component({
  selector: 'app-clientes',
  templateUrl: './clientes.component.html'
})
export class ClientesComponent implements OnInit {

  clientes: Cliente[];
  paginador: any;
  clienteSeleccionado: Cliente;

  constructor(
    private clienteService: ClienteService,
    private activatedRoute: ActivatedRoute,
    private  modalService: ModalService) {
  }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(params => {
      // operador suma convierte en número
      let page: number = +params.get('page');
      // Para la primera pagina no sirve el parametro
      if (!page) {
        page = 0;
      }
      this.clienteService.getClientes(page).pipe(
        tap( (response: any) => {
          console.log('ClientesComponent: Tap 3');
          (response.content as Cliente[]).forEach( cl => {
            console.log(cl.createdAt);
          });
        })
      ).subscribe(
        (response: any) => {
          this.clientes = (response.content as Cliente[]);
          this.paginador = response;
        }
      );
    });



    this.modalService.notificarUpload.subscribe( cliente => {
        this.clientes = this.clientes.map( c => {
        if (c.id === cliente.id) {
            c.foto = cliente.foto;
        }
        return c;
      });
    });
  }

  delete(cliente: Cliente): void {
    Swal.fire({
      title: '¿Seguro desea eliminar?',
      text: `¿Desea elminar al cliente ${cliente.nombre} ${cliente.apellido} de manera permanente?`,
      type: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'No, cancelar'
    }).then((result) => {
      if (result.value) {
        this.clienteService.delete(cliente.id).subscribe(
          respons => {
            this.clientes = this.clientes.filter( cli => cli !== cliente);
            Swal.fire(
              'Cliente eliminado',
              `Cliente ${cliente.nombre} ${cliente.apellido} eliminado con éxito`,
              'success'
            );
          }
        );
      }
    });
  }

  abrirModal(cliente: Cliente): void {
    this.clienteSeleccionado = cliente;
    this.modalService.abrirModal();
  }
}
