package com.santiago.ecom.services.admin.faq;

import com.santiago.ecom.dto.FAQDto;

public interface FAQService {

    FAQDto postFAQ(Long productId, FAQDto faqDto);
}
