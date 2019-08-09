import { Injectable } from '@angular/core';
import { DatePipe } from '@angular/common';
import {Cliente} from './cliente';
import { of, Observable, throwError } from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { map, catchError, tap } from 'rxjs/operators';
import Swal from 'sweetalert2';
import {Router} from '@angular/router';

// import {CLIENTES} from './clientes.json'


@Injectable({
  providedIn: 'root'
})
export class ClienteService {

  private urlEndPoint: string = 'http://localhost:8080/api/clientes';
  constructor(private http: HttpClient, private  router: Router) {}
  private httpHeaders = new  HttpHeaders({'Content-Type': 'application/json'})

  getClientes(page: number): Observable<Cliente[]> {
    return this.http.get(this.urlEndPoint + '/page/' + page).pipe(
      // tap manipula sin modificar y cambia el flujo de datos
      tap( (response: any )  => {
            console.log('ClienteService: Tap 1');
            (response.content as Cliente[]).forEach( cl => {
            console.log(cl.nombre);
          });
        }),
      // operador map si modifica al realizar el return la respuesta a clientes[]
      map( (response: any)  => {
        // Formato para cada cliente con función map
        (response.content as Cliente[]).map( cli => {
          cli.nombre = cli.nombre.toUpperCase();
          // cli.createdAt = new DatePipe('es').transform(cli.createdAt, 'EEEE dd, MMMM yyyy');
          // cli.createdAt = formatDate(cli.createdAt, 'dd-MM-yyyy', 'en-US');
          return cli;
        });
        return response;
      } ),
      // Aqui ya son clientes modificados por map
      tap( (response: any) => {
          console.log('ClienteService: Tap 2 luego del map');
          (response.content as Cliente[]).forEach( cl => {
          console.log(cl.nombre);
        });
      }),
    );
    // return this.http.get<Cliente[]>(this.urlEndPoint);
    // return of(CLIENTES);
  }

  create(cliente: Cliente): Observable<Cliente> {
    return this.http.post(this.urlEndPoint, cliente, { headers: this.httpHeaders}).pipe(
      map( (response: any) => response.cliente as Cliente ),
      catchError(e => {

        if (e.status === 400) {
          return throwError(e) ;
        }

        console.error(e.error.mensaje);
        Swal.fire(e.error.mensaje, e.error.error, 'error');
        return throwError(e);
      })
    );
  }

  getCliente(id): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.urlEndPoint}/${id}`).pipe(
      catchError (e => {
        this.router.navigate(['/clientes']);
        console.error(e.error.mensaje);
        Swal.fire('Error al editar', e.error.mensaje, 'error');
        return throwError(e);
      })
    );
  }

  update(cliente: Cliente): Observable<any> {
    return this.http.put<any>(
      `${this.urlEndPoint}/${cliente.id}`,
      cliente,
      { headers: this.httpHeaders}).pipe(
        catchError(e => {

          if (e.status === 400) {
            return throwError(e) ;
          }

          console.error(e.error.mensaje);
          Swal.fire(e.error.mensaje, e.error.error, 'error');
          return throwError(e);
        })
    );
  }

  delete(id: number): Observable<Cliente> {
    return this.http.delete<Cliente>(`${this.urlEndPoint}/${id}`, {headers: this.httpHeaders}).pipe(
      catchError(e => {
        console.error(e.error.mensaje);
        Swal.fire(e.error.mensaje, e.error.error, 'error');
        return throwError(e);
      })
    );
  }
}
