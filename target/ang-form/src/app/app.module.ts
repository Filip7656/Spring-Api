import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { HttpModule } from '@angular/http';
import { AppComponent } from './app.component';
import { Ng2SmartTableModule } from 'ng2-smart-table';
import { TableComponent } from './table/table.component';
import { TableService } from './table/table.service';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    HttpModule,
    Ng2SmartTableModule,


  ],
  declarations: [
    AppComponent,
    TableComponent
  ],
  bootstrap: [AppComponent],
  providers: [TableService]
})

export class AppModule { }
