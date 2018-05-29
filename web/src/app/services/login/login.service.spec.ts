import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { LoginService } from './login.service'
import { User } from '../../models/user'
import { UserService } from '../user/user.service'

class UserServiceMock{
    user: User;
    constructor(){}
    setUser = (u:User)=>{}
    getUser = ()=>{}
    getToken = ()=>{}
    clean = () =>{}
}

describe('LoginService', ()=>{
    let service: LoginService;
    let httpMock: HttpTestingController;


    const spy = jasmine.createSpyObj('UserService', ['setUser']);

    beforeEach(() => {
        TestBed.configureTestingModule({
          imports: [HttpClientTestingModule],
          providers: [LoginService, { provide: UserService, useValue: spy }]
        });
        // inject the service
        service = TestBed.get(LoginService);
        httpMock = TestBed.get(HttpTestingController);
    });

    it('should login the data successful', () => {
        // test goes here
        service.token(new User("erikschmoll", "root")).subscribe((data: any) => {
            expect(data).toBe(true);
          });
        const req = httpMock.expectOne(`http://127.0.0.1:8081/api/token`, 'call to api');
        expect(req.request.method).toBe('POST');
    
        req.flush(
            {
                "metadata": {
                  "version": "v1"
                },
                "token": {
                  "id": 1,
                  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7InVzZXJuYW1lIjoiZXJpa3NjaG1vbGwiLCJyb2xlIjoiYWRtaW4ifSwiaWF0IjoxNTI3NTYxNjgxLCJleHAiOjE1Mjc1NjUyODF9.yp_OS-rG9uPt8QoCNsdxNeyqHZxei6K6-93cvTTBaXc",
                  "expiresAt": 3600
                }
              }
        );
    
        httpMock.verify();
     });

})

