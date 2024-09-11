from django.urls import path
from . import views

urlpatterns = [
    path('', views.index),
    path('create-new-bill/', views.create_new_bill, name='create-new-bill'),
    path('generate-invoice/<str:invoice_id>', views.generate_invoice, name='generate-invoice'),
]