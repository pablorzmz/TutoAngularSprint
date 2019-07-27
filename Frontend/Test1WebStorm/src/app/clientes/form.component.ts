import { Component, OnInit } from '@angular/core';
import {Cliente} from './cliente';
import {ClienteService} from './cliente.service';
import {ActivatedRoute, Router} from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html'
})
export class FormComponent implements OnInit {
  private cliente: Cliente = new Cliente()
  private titulo  = 'Crear Cliente'
  constructor(
    private clienteService: ClienteService,
    private router: Router,
    private activateRoute: ActivatedRoute
  ) { }

  ngOnInit() {
    this.cargarCliente();
  }

  cargarCliente(): void {
    this.activateRoute.params.subscribe(params => {
      const llave = 'id';
      const id = params[llave];
      if (id) {
        this.clienteService.getCliente(id).subscribe(
          (cliente) => this.cliente = cliente
        );
      }
    });
  }

   create(): void {
    this.clienteService.create(this.cliente).subscribe(
      nuevoCliente => {
        this.router.navigate(['/clientes']);
        Swal.fire(
          'Nuevo cliente',
          `Cliente ${nuevoCliente.nombre} creado con éxito`,
          'success');
      }
    );
  }

  update(): void {
    this.clienteService.update(this.cliente).subscribe(
      cliente => {
        this.router.navigate(['/clientes']);
        Swal.fire(
          'Cliente actualizado',
          `Cliente ${cliente.nombre} actualizado con éxito`,
          'success'
        );
      }
    );
  }
}
