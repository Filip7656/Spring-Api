import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Http } from '@angular/http';
import { HttpHeaders } from '@angular/common/http';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type':  'application/json',
  })
};

@Component({
  selector: 'app',
  templateUrl: 'app.component.html'
})

export class AppComponent {
  model: any = {};


  constructor(private http:HttpClient) {
     }

  onSubmit() {
    //alert('SUCCESS!! :-)\n\n' + JSON.stringify(this.model))
    //
    this.http.post('http://145.239.87.1:8080/json',  JSON.stringify(this.model),httpOptions)
      .subscribe(
        res => {
          console.log(res);
        },
        err => {
          console.log("Error occured" + err);
        }
      );

  }
}
