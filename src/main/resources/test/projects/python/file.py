from django.http import HttpResponse, HttpResponseRedirect
from django.template import loader
from django.shortcuts import get_object_or_404,render
from django.urls import reverse
from django.views import generic

from .models import Choice, Question

import datetime
from django.utils import timezone

# Create your views here.

class IndexView(generic.ListView):
	template_name='firstapp/index.html'
	context_object_name='latest_question_list'

	def get_queryset(self):
		return Question.objects.order_by('pub_date')

def addq(request):
	if request.method == 'POST':
		newq=request.POST.get('new_question')
		q = Question(question_text=newq,pub_date=timezone.now())
		q.save()
		#newc=request.POST.get('new_choice')
		#choice_question=question_id
		#c=Choice(choice_text=newc,question=choice_question)
		#c.save()
		return render(request,'firstapp/addq.html')
	else:	
		return render(request,'firstapp/addq.html')

class DetailView(generic.DetailView):
	model=Question
	template_name='firstapp/detail.html'

class ResultsView(generic.DetailView):
	model=Question
	template_name='firstapp/results.html'


def vote(request,question_id):
	question = get_object_or_404(Question, pk=question_id)
	try:
		selected_choice = question.choice_set.get(pk=int(request.POST['choice']))
	except (KeyError, Choice.DoesNotExist):
		return render(request, 'firstapp/detail.html', {
			'question': question,
			'error_message': "You didn't select a choice.",
		})
	else:
		selected_choice.votes += 1
		selected_choice.save()
		return HttpResponseRedirect(reverse('firstapp:results', args=(question.id,)))