from django.shortcuts import render
from django.conf import settings
from django.http import JsonResponse
#from weasyprint import HTML, CSS
from bson.objectid import ObjectId
from django.template.loader import get_template
from django.http import HttpResponse
from .models import billings_collection

def index(request):
    return HttpResponse("hello world")

def create_new_bill(request):
    newBill = {
        "userId": "id",
        "amount": "amount",
        "status": "paid",
        "paymentreference": "refernece"
    }
    bill_id = billings_collection.insert_one(newBill).inserted_id
    return JsonResponse({"invoice": str(bill_id)})


def generate_invoice(request, invoice_id):
    bill = billings_collection.find_one({"_id": ObjectId(invoice_id)})
    html_template = get_template('invoice.html')
    
    print(bill.get('_id'))
    template_vars = {
        "invoice_id": bill.get('_id')
    }
    
    html_out = html_template.render(template_vars)
    
    #response = HttpResponse(pdf_file, content_type='application/pdf')
    #response['Content-Disposition'] = 'filename="home_page.pdf"'
    return HttpResponse(html_out)