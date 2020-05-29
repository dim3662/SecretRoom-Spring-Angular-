import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-secret',
  templateUrl: './secret.component.html',
  styleUrls: ['./secret.component.css']
})
export class SecretComponent implements OnInit {

  public id: String;


  constructor(private http: HttpClient, private route: ActivatedRoute, private router: Router) {
    this.route.params.subscribe(params => this.id = params.id);
  }

  public responce;

  ngOnInit(): void {
    this.http.get(
      'http://localhost:8080/message/' + this.id).subscribe(response => {
      this.responce = response;
      this.checkPass();
    });

  }

  public check: boolean;

  checkPass() {
    if (this.responce.password.length != 0 && this.responce.show == false)
      return this.check = true;
    else {
      this.see = true;
      return this.check = false;
    }
  }

  public see: boolean = false;

  public password = {
    password: '',
  };
  public checkPassword;

  onSubmit() {
    this.http.post(
      'http://localhost:8080/message/checkPassword/' + this.id,
      this.password, {
        headers: new HttpHeaders().set('Content-type', 'application/json'),
      }).subscribe(response => {
      this.checkPassword = response;
      if (this.checkPassword.check) {
        this.responce.message.message = this.checkPassword.message;
        return this.see = true;
      }
    })
  }

  goHome() {
    this.router.navigate(["/"]);
  }
}
