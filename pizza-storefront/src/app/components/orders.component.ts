import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PizzaService } from '../pizza.service';
import { OrderSummary } from '../models';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {

  email!: string
  orders: OrderSummary[] = []

  constructor(private route: ActivatedRoute, private pizzaSvc: PizzaService, private router: Router) { }

  ngOnInit(): void {
    this.email = this.route.snapshot.params['email']
    console.info(">>> email: ", this.email)
    this.pizzaSvc.getOrders(this.email)
      .then(result => {
        this.orders = result
        console.info(">> orders: ", this.orders)
      }).catch(error => {
        console.error(">> error: ", error)
      })
  }

  goBack() {
    this.router.navigate(['/'])
  }

}
