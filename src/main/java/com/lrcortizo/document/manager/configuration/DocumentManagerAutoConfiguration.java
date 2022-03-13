package com.lrcortizo.document.manager.configuration;

import com.lrcortizo.document.manager.service.DocumentManagerService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DocumentManagerService.class
})
public class DocumentManagerAutoConfiguration {
}
