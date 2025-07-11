import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

// Angular Material Modules
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatButtonModule } from '@angular/material/button';

// HTTP Client for API calls
import { HttpClientModule } from '@angular/common/http';

// Custom Components
import { NavbarComponent } from './components/navbar/navbar.component';
import { UploadComponent } from './components/upload/upload.component';

import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    UploadComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatSnackBarModule,
    MatToolbarModule,
    MatProgressBarModule,
    MatButtonModule,
    MatCardModule,
    MatChipsModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
