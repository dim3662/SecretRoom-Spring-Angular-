import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-new-secret',
  templateUrl: './new-secret.component.html',
  styleUrls: ['./new-secret.component.css']
})
export class NewSecretComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }
goHome(){
  this.router.navigate(["/"]);
}
}
