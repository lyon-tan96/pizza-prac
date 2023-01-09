import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Order } from '../models';
import { PizzaService } from '../pizza.service';

const SIZES: string[] = [
  "Personal - 6 inches",
  "Regular - 9 inches",
  "Large - 12 inches",
  "Extra Large - 15 inches"
]

const PizzaToppings: string[] = [
    'chicken', 'seafood', 'beef', 'vegetables',
    'cheese', 'arugula', 'pineapple'
]

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  pizzaSize = SIZES[0]
  
  form!: FormGroup
  toppings!: FormArray

  constructor(private fb: FormBuilder, private router: Router, private pizzaSvc: PizzaService) {}

  ngOnInit(): void {
    this.form = this.createForm()
  }

  updateSize(size: string) {
    this.pizzaSize = SIZES[parseInt(size)]
  }

  processOrder() {
    console.info('>>> order: ', this.form.value)
    const order = this.form.value as Order
    this.pizzaSvc.createOrder(order)
      .then(result => {
        console.info('>>> process order: ', result)
        this.router.navigate(['/orders/' + order.email])
      }).catch(error => (
        console.error(">>> error: ", error)
      ))
  }

  onChange(e: any) {
    const toppings: FormArray = this.form.get('toppings') as FormArray
    if (e.target.checked) {
      toppings.push(new FormControl(e.target.value))
    } else {
      let i: number = 0
      toppings.controls.forEach((item: any) => {
        if (item.value == e.target.value) {
          toppings.removeAt(i)
          return
        }
        i++
      })
    }
    console.info('>> toppings: ', toppings.value)
  }

  createForm() {
    this.toppings = this.fb.array([], [Validators.required, Validators.min(1)])
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required]),
      email: this.fb.control<string>('', [Validators.required]),
      size: this.fb.control<number>(0, [Validators.required]),
      base: this.fb.control<string>('', [Validators.required]),
      sauce: this.fb.control<string>('classic', [Validators.required]),
      toppings: this.toppings,
      comments: this.fb.control<string>('')
    })
  }

  viewOrders() {
    const email = this.form.get('email')?.value
    this.router.navigate(['/orders/' + email])
  }

}
