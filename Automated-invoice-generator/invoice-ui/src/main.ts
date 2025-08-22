import { bootstrapApplication } from '@angular/platform-browser';
import { Component, effect, inject, signal } from '@angular/core';

type Client = { id: number; name: string; email: string; address?: string; gstNumber?: string };
type Item = { description: string; quantity: number; unitPrice: number };
type Invoice = {
  id: number;
  client: Client;
  issueDate: string;
  dueDate: string;
  subTotal: number;
  taxPercent: number;
  taxAmount: number;
  totalAmount: number;
};

const API = 'http://localhost:8080/api';

@Component({
  selector: 'app-root',
  standalone: true,
  template: `
  <h1>Automated Invoice Generator</h1>

  <div class="card">
    <h2>Create Client</h2>
    <form (submit)="createClient($event)">
      <div class="row">
        <div>
          <label>Name</label>
          <input required name="name" />
          <label>Email</label>
          <input type="email" name="email" />
        </div>
        <div>
          <label>Address</label>
          <input name="address" />
          <label>GST Number</label>
          <input name="gstNumber" />
        </div>
      </div>
      <button type="submit">Add Client</button>
    </form>
  </div>

  <div class="card">
    <h2>New Invoice</h2>
    <div>
      <label>Client</label>
      <select [value]="selectedClientId()" (change)="selectedClientId.set(+(($event.target as HTMLSelectElement).value))">
        <option *ngFor="let c of clients()" [value]="c.id">{{c.name}}</option>
      </select>

      <label>Due Date</label>
      <input type="date" [value]="dueDate()" (change)="dueDate.set(($event.target as HTMLInputElement).value)" />
      <label>Tax %</label>
      <input type="number" [value]="taxPercent()" (input)="taxPercent.set(+($event.target as HTMLInputElement).value)" />
    </div>

    <div class="items-grid">
      <div><b>Description</b></div><div><b>Qty</b></div><div><b>Unit Price</b></div>

      <ng-container *ngFor="let it of items(); let i = index">
        <input [value]="it.description" (input)="updateItem(i, 'description', ($event.target as HTMLInputElement).value)" />
        <input type="number" [value]="it.quantity" (input)="updateItem(i, 'quantity', +($event.target as HTMLInputElement).value)" />
        <input type="number" [value]="it.unitPrice" (input)="updateItem(i, 'unitPrice', +($event.target as HTMLInputElement).value)" />
      </ng-container>
    </div>
    <button (click)="addItem()">+ Add Item</button>
    <button (click)="createInvoice()">Create Invoice</button>
  </div>

  <div class="card">
    <h2>Invoices</h2>
    <table>
      <thead><tr><th>ID</th><th>Client</th><th>Issue</th><th>Due</th><th>Total</th><th>PDF</th></tr></thead>
      <tbody>
        <tr *ngFor="let inv of invoices()">
          <td>{{inv.id}}</td>
          <td>{{inv.client.name}}</td>
          <td>{{inv.issueDate}}</td>
          <td>{{inv.dueDate}}</td>
          <td>{{inv.totalAmount}}</td>
          <td><a [href]="API + '/invoices/' + inv.id + '/pdf'">Download</a></td>
        </tr>
      </tbody>
    </table>
  </div>
  `
})
class AppComponent {
  API = API;
  clients = signal<Client[]>([]);
  invoices = signal<Invoice[]>([]);
  selectedClientId = signal<number>(1);
  dueDate = signal<string>(new Date().toISOString().slice(0,10));
  taxPercent = signal<number>(18);
  items = signal<Item[]>([{ description: 'Service', quantity: 1, unitPrice: 1000 }]);

  constructor(){
    this.loadClients();
    this.loadInvoices();
  }

  async loadClients(){
    const res = await fetch(API + '/clients');
    const data = await res.json();
    this.clients.set(data);
    if (data.length) this.selectedClientId.set(data[0].id);
  }
  async loadInvoices(){
    const res = await fetch(API + '/invoices');
    this.invoices.set(await res.json());
  }

  async createClient(ev: Event){
    ev.preventDefault();
    const form = ev.target as HTMLFormElement;
    const body = Object.fromEntries(new FormData(form).entries());
    const res = await fetch(API + '/clients', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    });
    if (res.ok){ form.reset(); await this.loadClients(); }
  }

  addItem(){ this.items.set([...this.items(), { description: '', quantity: 1, unitPrice: 0 }]); }
  updateItem(i: number, key: keyof Item, val: any){
    const next = this.items().slice();
    (next[i] as any)[key] = val;
    this.items.set(next);
  }

  async createInvoice(){
    const body = {
      clientId: this.selectedClientId(),
      dueDate: this.dueDate(),
      taxPercent: this.taxPercent(),
      items: this.items()
    };
    const res = await fetch(API + '/invoices', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    });
    if (res.ok){ await this.loadInvoices(); }
  }
}

bootstrapApplication(AppComponent);
