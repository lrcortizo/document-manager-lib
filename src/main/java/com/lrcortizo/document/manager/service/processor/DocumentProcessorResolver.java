package com.lrcortizo.document.manager.service.processor;

import com.lrcortizo.document.manager.exception.DocumentManagerException;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DocumentProcessorResolver {

    private final Map<String, DocumentProcessorService> documentProcessorServices;

    DocumentProcessorResolver(final List<DocumentProcessorService> documentProcessors) {
        this.documentProcessorServices = documentProcessors.stream()
                .collect(Collectors.toUnmodifiableMap(
                        service -> ClassUtils.getUserClass(service.getClass()).getSimpleName(),
                        service -> service));
    }

    public DocumentProcessorService resolve(final String resolverKey) {
        if (!this.documentProcessorServices.containsKey(resolverKey)) {
            throw new DocumentManagerException(String.format("Document processor service with name %s not found",
                    resolverKey));
        }
        return this.documentProcessorServices.get(resolverKey);
    }
}
