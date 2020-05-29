import {Component, ElementRef, Inject, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-private',
  templateUrl: './private.component.html',
  styleUrls: ['./private.component.css']
})
export class PrivateComponent implements OnInit {

  public id: String;
  public currentURL: String = window.location.href;

  constructor(private http: HttpClient, private route: ActivatedRoute, private router: Router) {
    this.route.params.subscribe(params => this.id = params.id);

  }

  select() {
    window.getSelection().removeAllRanges();
    var range = document.createRange();
    range.selectNode(document.getElementById("link"));
    window.getSelection().addRange(range);
  }

  public responce;
  public message;

  ngOnInit(): void {
    let len = window.location.href.length - this.id.length;
    this.currentURL = this.currentURL.substring(0, len - 8) + "secret/" + this.id;
    this.select();
    this.http.get(
      'http://localhost:8080/message/' + this.id).subscribe(response => {
      this.responce = response;
      this.message = response.toString();
    });

  }

  goHome() {
    this.router.navigate(["/"]);
  }

  delete() {
    this.http.delete(
      'http://localhost:8080/message/' + this.id).subscribe();
    this.router.navigate(["new"]);
  }

  emailBox = {
    url: this.currentURL,
    email: ''
  };
  public responceEmail;
  sendEmail() {
    let len = window.location.href.length - this.id.length;
    this.emailBox.url = this.currentURL.substring(0, len - 8) + "secret/" + this.id;
    this.http.post(
      'http://localhost:8080/message/sendEmail',
      this.emailBox, {
        headers: new HttpHeaders().set('Content-type', 'application/json'),
      }).subscribe(response => {
      this.responceEmail = response;
      this.emailBox.email="Ваше сообщение отправлено";
    });
  }
}
