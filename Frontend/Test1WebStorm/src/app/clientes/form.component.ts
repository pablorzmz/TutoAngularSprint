import { Component, OnInit } from '@angular/core';
import {Cliente} from './cliente';
import {ClienteService} from './cliente.service';
import {ActivatedRoute, Router} from '@angular/router';
import Swal from 'sweetalert2';
import {Region} from './region';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html'
})
export class FormComponent implements OnInit {
  private cliente: Cliente = new Cliente();
  private regiones: Region[];
  private titulo  = 'Crear Cliente';
  private errores: string[];

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
    this.clienteService.getRegiones().subscribe(regiones => {
      this.regiones = regiones;
    });
  }

   create(): void {
    this.clienteService.create(this.cliente).subscribe(
      cliente => {
        this.router.navigate(['/clientes']);
        Swal.fire(
          'Nuevo cliente',
          ` El cliente ${cliente.nombre} ha sido creado con éxito!`,
          'success');
      },
      err => {
        this.errores = err.error.errors as string [];
        console.error('Codigo error en el backend: ' + err.status);
        console.error(this.errores);
      }
    );
  }

  update(): void {
    this.clienteService.update(this.cliente).subscribe(
      response => {
        this.router.navigate(['/clientes']);
        Swal.fire(
          'Cliente actualizado',
          `${response.mensaje}: ${response.cliente.nombre}`,
          'success'
        );
      },
      err => {
        this.errores = err.error.errors as string [];
        console.error('Codigo error en el backend: ' + err.status);
        console.error(this.errores);
      }
    );
  }

  compararRegion(o1: Region, o2: Region): boolean {
      if ( o1 === undefined && o2 === undefined ) {
        return true;
      }
      return (o1 === null || o2 === null) || (o1 === undefined || o2 === undefined) ? false : (o1.id === o2.id && o1.nombre === o2.nombre);
  }
}
