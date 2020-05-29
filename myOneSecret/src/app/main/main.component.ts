import {Component, Inject, Injectable, OnInit, Renderer2} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Router} from "@angular/router";
import {DOCUMENT} from "@angular/common";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})

@Injectable()
export class MainComponent implements OnInit {

  constructor(private http: HttpClient, private router: Router) {

  }

  secret = {
    password: '',
    Message: {
      message: String
    },
    lifetime: '7 дней',
    message: undefined
  };

  public responce;

  public arr = [];

  ngOnInit(): void {
  }

  public id: String;


  onSubmit() {
    this.http.post(
      'http://localhost:8080/message',
      this.secret, {
        headers: new HttpHeaders().set('Content-type', 'application/json'),
      }).subscribe(response => {
      this.responce = response;
      this.router.navigate(["private/" + this.responce.toString().substr(30, this.responce.toString().length)]);
    });
  }

  public strLetter = "0123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
  generatePassword() {
    this.secret.password="";
    for (var i = 0; i < 10; i++) {
      this.secret.password += this.strLetter.toString().charAt(Math.random()*100 % 62);
    }
  }
}
