package com.lrcortizo.document.manager.toolkit.configuration;

import com.lrcortizo.document.manager.toolkit.service.DocumentManagerService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        DocumentManagerService.class
})
public class DocumentManagerAutoConfiguration {
}
