import {Component, Input, OnInit} from '@angular/core';
import {Cliente} from '../cliente';
import {ClienteService} from '../cliente.service';
import {ActivatedRoute} from '@angular/router';
import Swal from 'sweetalert2';
import {HttpEventType} from '@angular/common/http';
import {ModalService} from './modal.service';

@Component({
  selector: 'app-detalle-cliente',
  templateUrl: './detalle.component.html',
  styleUrls: ['./detalle.component.css']
})
export class DetalleComponent implements OnInit {

  @Input() cliente: Cliente;
  titulo: string = 'Detalle del cliente';
  private imagenSelecionada: File;
  progreso: number;

  constructor(private clienteService: ClienteService,
              private activatedRoute: ActivatedRoute,
              private modalService: ModalService) { }

  ngOnInit() {
    // obtener el id del parametro para encontrar el cliente
    /*
    this.activatedRoute.paramMap.subscribe( params => {
        const id: number = + params.get('id');
        if (id) {
            this.clienteService.getCliente(id).subscribe(cliente => {
              this.cliente = cliente;
            });
        }
      }
    );
    */
  }

  seleccionarFoto(event) {
    this.imagenSelecionada = event.target.files[0];
    this.progreso = 0;
    if (this.imagenSelecionada.type.indexOf('image') < 0) {
      Swal.fire('Error seleccionar imagen:', 'Debe seleccionar una foto', 'error');
      this.imagenSelecionada = null;
    }
    console.log(this.imagenSelecionada);
  }

  subirFoto() {
    if (!this.imagenSelecionada) {
        Swal.fire('Error upload:', 'Debe seleccionar una foto', 'error');
    } else {
      this.clienteService.subirFoto( this.imagenSelecionada, this.cliente.id).subscribe(
        // Viene con nueva foto
        event => {
          if (event.type === HttpEventType.UploadProgress) {
              this.progreso = Math.round(100 * event.loaded / event.total);
          } else if  (event.type === HttpEventType.Response) {
            const response: any = event.body;
            this.cliente = response.cliente as Cliente;

            // se emite el cambio de la foto del cliente
            this.modalService.notificarUpload.emit(this.cliente);

            Swal.fire('La foto se ha subido por completo!', response.mensaje, 'success');
          }
        }
      );
    }
  }


  cerrarModal() {
    this.modalService.cerrarModal();
    this.imagenSelecionada = null;
    this.progreso = 0;
  }

}
