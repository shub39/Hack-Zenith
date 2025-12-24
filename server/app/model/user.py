from pydantic import BaseModel, EmailStr

class User(BaseModel):
    id: int
    name: str
    email: EmailStr
    is_active: bool = True      
    is_superuser: bool = False
    is_verified: bool = False
    created_at: str
    updated_at: str
    class Config:
        orm_mode = True
    schema_extra = {
        "example": {
            "id": 1,
            "name": "John Doe",
            "email": "john.doe@example.com",
            "is_active": True,
            "is_superuser": False,
            "is_verified": True,
            "created_at": "2023-01-01T12:00:00Z",
            "updated_at": "2023-01-02T12:00:00Z"
        }
    }