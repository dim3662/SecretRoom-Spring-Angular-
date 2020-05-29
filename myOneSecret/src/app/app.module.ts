import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";

import { RouterModule, Routes } from '@angular/router';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MainComponent } from './main/main.component';
import { PrivateComponent } from './private/private.component';
import { SecretComponent } from './secret/secret.component';
import { NewSecretComponent } from './new-secret/new-secret.component';
const appRoutes: Routes = [
  { path: '', component: MainComponent },
  { path: 'private/:id', component: PrivateComponent},
  { path: 'secret/:id', component: SecretComponent},
  { path: 'new', component: NewSecretComponent},
  ];
@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    PrivateComponent,
    SecretComponent,
    NewSecretComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule.forRoot(appRoutes,
    { enableTracing: true })
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
