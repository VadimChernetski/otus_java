import { Injectable } from "@angular/core";
import { environment } from "../../environments/environment";
import { Observable } from "rxjs";
import { HttpClient, HttpContext, HttpHeaders, HttpParams } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class HttpWrapperService {

  endpointPrefix = environment.endpoint;

  constructor(private httpClient: HttpClient) {
  }

  public get<T>(url: string, options?: {
    headers?: HttpHeaders | {
      [header: string]: string | string[];
    };
    context?: HttpContext;
    observe?: 'body';
    params?: HttpParams | {
      [param: string]: string | number | boolean | ReadonlyArray<string | number | boolean>;
    };
    reportProgress?: boolean;
    responseType?: 'json';
    withCredentials?: boolean;
  }): Observable<T> {
    const optionsWithCredentials = options ? { ...options } : {};
    optionsWithCredentials.withCredentials = true;
    return this.httpClient.get<T>(url, optionsWithCredentials);
  }

  public post<T>(url: string, body: any, options?: {
    headers?: HttpHeaders | { [header: string]: string | string[] };
    context?: HttpContext;
    observe?: 'body';
    params?: HttpParams | {
      [param: string]: string | number | boolean | ReadonlyArray<string | number | boolean>;
    };
    reportProgress?: boolean;
    responseType?: 'json';
    withCredentials?: boolean;
  }): Observable<T> {
    const optionWithCredentials = options ? { ...options } : {};
    optionWithCredentials.withCredentials = true;
    return this.httpClient.post<T>(url, body, optionWithCredentials);
  }

}
