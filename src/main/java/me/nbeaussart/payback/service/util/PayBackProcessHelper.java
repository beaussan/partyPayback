package me.nbeaussart.payback.service.util;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import me.nbeaussart.payback.domain.Event;
import me.nbeaussart.payback.domain.ExtandedUser;
import me.nbeaussart.payback.domain.InitialPayment;
import me.nbeaussart.payback.domain.PayBack;
import me.nbeaussart.payback.service.PayBackService;

public class PayBackProcessHelper {

	public PayBackProcessHelper() {
	}

	public Double getTotalAmmountPerPerson(Set<InitialPayment> initialPayments, Set<ExtandedUser> participants) {
		Double totalAmmount = 0D;

		for (InitialPayment initialPayment : initialPayments) {
			totalAmmount += initialPayment.getAmmount();
		}

		return totalAmmount / participants.size();
	}

	public Map<ExtandedUser, Double> getAllPaymentPerPerson(Set<InitialPayment> initialPayments) {
		Map<ExtandedUser, Double> paymentPerPerson = new HashMap<ExtandedUser, Double>();
		for (InitialPayment initialPayment : initialPayments) {
			ExtandedUser user = initialPayment.getUser();
			Double ammount = initialPayment.getAmmount();
			ammount += paymentPerPerson.containsKey(user) ? paymentPerPerson.get(user) : 0;
			paymentPerPerson.put(user, ammount);
		}
		return paymentPerPerson;
	}

	public void getDebtorsAndCreditors(Map<ExtandedUser, Double> participantPayments, Double ammountPerUser,
			Map<ExtandedUser, Double> debtors, Map<ExtandedUser, Double> creditors) {
		for (Entry<ExtandedUser, Double> entry : participantPayments.entrySet()) {
			Double value = ammountPerUser - entry.getValue();
			if (value > 0) {
				debtors.put(entry.getKey(), value);
			} else if (value < 0) {
				creditors.put(entry.getKey(), Math.abs(value));
			}
		}
	}

	public Set<PayBack> getNewPayBacks(Event event, Set<PayBack> paybacks, Map<ExtandedUser, Double> debtors, Map<ExtandedUser, Double> creditors, PayBackService payBackService) {
		Set<PayBack> newPayBacks = new HashSet<PayBack>();
		
		manageOldPayBacks(paybacks, debtors, creditors, payBackService, newPayBacks);
		
		sortMapByValue(creditors);
		sortMapByValue(debtors);        
        
		createNewPayBacks(creditors, debtors, event, payBackService, newPayBacks);
		
		return newPayBacks;
	}

	private void sortMapByValue(Map<ExtandedUser, Double> map) {
        map.entrySet().stream().sorted(Map.Entry.<ExtandedUser, Double>comparingByValue());
	}

	private void manageOldPayBacks(Set<PayBack> paybacks, Map<ExtandedUser, Double> debtors,
			Map<ExtandedUser, Double> creditors, PayBackService payBackService, Set<PayBack> newPayBacks) {
        for (PayBack payback : paybacks){
        	if (payback.isIsPaid()){
            	ExtandedUser source = payback.getSource();
            	ExtandedUser toPay = payback.getToPay();
            	Double ammount = payback.getAmmount();
            	if (creditors.containsKey(source)){
            		creditors.put(source, creditors.get(source)+ammount);
            	} else {
            		Double newSolde = debtors.get(source)-ammount;
            		if (newSolde < 0){
            			debtors.remove(source);
            			creditors.put(source, Math.abs(newSolde));
            		} else {
            			debtors.put(source, newSolde);
            		}
            	}
            	if (creditors.containsKey(toPay)){
            		Double newSolde = creditors.get(toPay) - ammount;
            		if (newSolde < 0){
            			creditors.remove(toPay);
            			debtors.put(toPay, Math.abs(newSolde));
            		} else {
            			creditors.put(toPay, newSolde);
            		}
            	} else {
            		debtors.put(toPay, debtors.get(toPay) + ammount);
            	}
            	newPayBacks.add(payback);
        	} else {
        		payBackService.delete(payback.getId());
        	}
        }
	}

	private void createNewPayBacks(Map<ExtandedUser, Double> creditors, Map<ExtandedUser, Double> debtors, Event event,
			PayBackService payBackService, Set<PayBack> newPayBacks) {
        for (Entry<ExtandedUser, Double> creditor : creditors.entrySet()){
        	Double solde = creditor.getValue();
        	while (solde > 0){
        		Entry<ExtandedUser, Double> debtor = debtors.entrySet().iterator().next();
        		Double debt = solde - debtor.getValue();
        		PayBack payback = new PayBack();
        		if (debt <= 0){
        			debtor.setValue(Math.abs(debt));
        			payback.setAmmount(Math.abs(solde));
        			solde -= Math.abs(solde);
        		} else {
        			payback.setAmmount(debtor.getValue());
        			solde -= Math.abs(debtor.getValue());
        			debtors.remove(debtor.getKey());
        		}
        		saveNewPayBack(event, debtor.getKey(), creditor.getKey(), payback, newPayBacks, payBackService);
        	}
        }
	}

	private void saveNewPayBack(Event event, ExtandedUser debtor, ExtandedUser creditor, PayBack payback,
			Set<PayBack> newPayBacks, PayBackService payBackService) {
		payback.setEvent(event);
		payback.setIsPaid(false);
		payback.setSource(debtor);
		payback.setToPay(creditor);
		payback.setTimestamp(ZonedDateTime.now());
		newPayBacks.add(payback);
		payBackService.save(payback);
	}
}
