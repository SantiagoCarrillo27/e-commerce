package com.santiago.ecom.services.admin.faq;

import com.santiago.ecom.dto.FAQDto;
import com.santiago.ecom.entity.FQA;
import com.santiago.ecom.entity.Product;
import com.santiago.ecom.repository.FAQRepository;
import com.santiago.ecom.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FAQServiceImpl implements FAQService {

    private final FAQRepository faqRepository;

    private final ProductRepository productRepository;


    public FAQDto postFAQ(Long productId, FAQDto faqDto){
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()){
            FQA faq = new FQA();


            faq.setQuestion(faqDto.getQuestion());
            faq.setAnswer(faqDto.getAnswer());
            faq.setProduct(optionalProduct.get());

            return faqRepository.save(faq).getFAQDto();

        }
        return null;
    }

}
