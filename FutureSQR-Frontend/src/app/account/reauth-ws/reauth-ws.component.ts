import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpXsrfTokenExtractor } from '@angular/common/http';
import { first } from 'rxjs/operators';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-reauth-ws',
  templateUrl: './reauth-ws.component.html',
  styleUrls: ['./reauth-ws.component.css']
})
export class ReauthWsComponent implements OnInit {

  private returnUrl: string

  token: string | null = null
  tokenDefinition: CsrfToken | null = null

  constructor(extraktor: HttpXsrfTokenExtractor, private http: HttpClient, private router: Router, route: ActivatedRoute) {
    this.returnUrl = route.snapshot.queryParams['returnUrl'] || '/';

    this.token = extraktor.getToken()
  }

  ngOnInit(): void {
    this.http.get<CsrfToken>("/FutureSQR/rest/login/csrf").pipe(first()).subscribe({
      next: n => { this.tokenDefinition = n },
      // timeout is only for debugging purpose and may be removed later
      complete: () => { setTimeout(() => this.router.navigate(['/', 'account', 'login'], { queryParams: { returnUrl: this.returnUrl } }), 3000) }
    })
  }
}

interface CsrfToken {
  headerName: String
  parameterName: String
  token: String
}
