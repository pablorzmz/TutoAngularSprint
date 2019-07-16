import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title: string = 'Bienvenido a angular!';
  curso: string = 'Curso web robao de spring 5 y angular 7';
  estudiante: string = 'Jose Pablo Ramírez Méndez';
}
