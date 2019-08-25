import {EventEmitter, Injectable} from '@angular/core';
import {Cliente} from '../cliente';

@Injectable({
  providedIn: 'root'
})
export class ModalService {

  modal: boolean = false;
  private  _notificarUpload = new  EventEmitter<any>();

  constructor() { }


  get notificarUpload(): EventEmitter<any> {
      return this._notificarUpload;
  }

  cerrarModal() {
    this.modal = false;
  }

  abrirModal() {
    this.modal = true;
  }
}
