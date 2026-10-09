
package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomerCommand;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.event.CustomerUpdatedEvent;
import kh.edu.istad.platform.customer.domain.exception.CustomerNotFoundException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.domain.service.CustomerDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateCustomerUseCase {

    private final CustomerRepository customerRepository;
    private final CustomerDomainService customerDomainService;

    public CustomerUpdatedEvent execute(UpdateCustomerCommand command) {

        // 1. Load the existing customer
        Customer customer = customerRepository.findById(
                new CustomerId(command.customerId())
        ).orElseThrow(() ->
                new CustomerNotFoundException(command.customerId())
        );

        // 2. Update the customer and create the event
        CustomerUpdatedEvent event = customerDomainService.updateCustomer(
                customer,
                command.familyName(),
                command.givenName()
        );

        // 3. Save the updated customer
        customerRepository.save(customer);

        // 4. Return the event
        return event;
    }
}
