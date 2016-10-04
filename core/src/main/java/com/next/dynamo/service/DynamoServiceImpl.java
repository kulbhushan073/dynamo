package com.next.dynamo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.next.dynamo.exception.DynamoException;
import com.next.dynamo.persistance.CustomDataPlugin;
import com.next.dynamo.persistance.Domain;
import com.next.dynamo.persistance.DomainTemplate;
import com.next.dynamo.persistance.PageTemplate;
import com.next.dynamo.persistance.StaticDataPlugin;
import com.next.dynamo.persistance.UrlMapping;
import com.next.dynamo.persistance.UrlMappingPlugin;
import com.next.dynamo.persistance.repository.CustomDataPluginRepository;
import com.next.dynamo.persistance.repository.DomainRepository;
import com.next.dynamo.persistance.repository.DomainTemplateRepository;
import com.next.dynamo.persistance.repository.PageTemplateRepository;
import com.next.dynamo.persistance.repository.StaticDataPluginRepository;
import com.next.dynamo.persistance.repository.UrlMappingPluginRepository;
import com.next.dynamo.persistance.repository.UrlMappingRepository;
import com.next.dynamo.util.DynamoAssert;

@Service
@Transactional(rollbackFor=Exception.class)
public class DynamoServiceImpl implements DynamoService {

	@Autowired
	private DomainRepository domainRepository;
	@Autowired
	private DomainTemplateRepository domainTemplateRepository;
	@Autowired
	private PageTemplateRepository pageTemplateRepository;
	@Autowired
	private CustomDataPluginRepository customDataPluginRepository;
	@Autowired
	private StaticDataPluginRepository staticDataPluginRepository;
	@Autowired
	private UrlMappingRepository urlMappingRepository;
	@Autowired
	private UrlMappingPluginRepository urlMappingPluginRepository;
	@Autowired
	private DynamoAssert dynamoAssert;

	@Override
	public Page<Domain> getDomains(int pageNumber, int pageSize) throws DynamoException {
		Sort sort = new Sort(new Order(Direction.ASC, "name"));
		PageRequest page = new PageRequest(pageNumber, pageSize, sort);
		Page<Domain> domains = domainRepository.findAll(page);
		return domains;
	}

	@Override
	public Domain saveDomain(Domain domain) throws DynamoException {
		if(domain.getExtendedDomain() != null){
			Domain extendedDomain = domainRepository.findOne(domain.getExtendedDomain().getId());
			domain.setExtendedDomain(extendedDomain);
		}
		domain = domainRepository.save(domain);
		return domain;
	}

	@Override
	public Domain getDomainById(Long domainId) throws DynamoException {
		return domainRepository.findOne(domainId);
	}

	@Override
	public DomainTemplate saveDomainTemplate(DomainTemplate domainTemplate) throws DynamoException {
		dynamoAssert.notNull(domainTemplate.getDomain(), "{domiantemplate.domain.empty.error}");
		DomainTemplate currentActiveDomainTemplate = domainTemplateRepository.findActiveDomainTemplateByDomainId(domainTemplate.getDomain().getId());
		if(currentActiveDomainTemplate!=null && domainTemplate.isActive() && !currentActiveDomainTemplate.getId().equals(domainTemplate.getId())){
			throw new DynamoException("{active.domain.template.exists.error}");
		}
		domainTemplate = domainTemplateRepository.save(domainTemplate);
		return domainTemplate;
	}

	@Override
	public DomainTemplate getDomainTemplateById(Long domainTemplateId) throws DynamoException {
		return domainTemplateRepository.findOne(domainTemplateId);
	}
	
	@Override
	public DomainTemplate getActiveDomainTemplateOfDomain(Long domainId) throws DynamoException {
		return domainTemplateRepository.findActiveDomainTemplateByDomainId(domainId);
	}
	
	@Override
	public List<DomainTemplate> getDomainTemplatesOfDomain(Long domainId) throws DynamoException{
		return domainTemplateRepository.findDomainTemplateByDomainId(domainId);
	}


	@Override
	public CustomDataPlugin saveCustomDataPlugin(CustomDataPlugin customDataPlugin) throws DynamoException {
		CustomDataPlugin dbCustomDataPlugin = customDataPluginRepository.findByFullClassName(customDataPlugin.getFullClassName());
		if(dbCustomDataPlugin != null){
			dbCustomDataPlugin.setPluginName(customDataPlugin.getPluginName());
			dbCustomDataPlugin.setDisabled(customDataPlugin.isDisabled());
			customDataPlugin = customDataPluginRepository.save(dbCustomDataPlugin);
		}else{
			customDataPlugin = customDataPluginRepository.save(customDataPlugin);
		}
		return customDataPlugin;
	}
	
	@Override
	public CustomDataPlugin getCustomDataPluginById(Long customDataPluginId) throws DynamoException {
		return customDataPluginRepository.findOne(customDataPluginId);
	}

	@Override
	public StaticDataPlugin saveStaticDataPlugin(StaticDataPlugin staticDataPlugin) throws DynamoException {
		staticDataPlugin = staticDataPluginRepository.save(staticDataPlugin);
		return staticDataPlugin;
	}

	@Override
	public StaticDataPlugin getStaticDataPluginById(Long staticDataPluginId) throws DynamoException {
		return staticDataPluginRepository.findOne(staticDataPluginId);
	}

	@Override
	public UrlMapping saveUrlMapping(UrlMapping urlMapping) throws DynamoException {
		urlMapping = urlMappingRepository.save(urlMapping);
		return urlMapping;
	}

	@Override
	public UrlMapping getUrlMappingById(Long urlMappingId) throws DynamoException {
		return urlMappingRepository.findOne(urlMappingId);
	}
	
	@Override
	public List<UrlMapping> getUrlMappingByDomainId(Long domainId) throws DynamoException{
		return urlMappingRepository.findUrlMappingByDomainId(domainId);
	}
	
	@Override
    public List<UrlMapping> getAllUrlMappings() throws DynamoException {
        return urlMappingRepository.findAll();
    }

	@Override
	public UrlMappingPlugin saveUrlMappingPlugin(UrlMappingPlugin urlMappingPlugin) throws DynamoException {
		urlMappingPlugin = urlMappingPluginRepository.save(urlMappingPlugin);
		return urlMappingPlugin;
	}
	
	@Override
	public List<UrlMappingPlugin> findUrlMappingPluginByUrlMapping(Long urlMappingId) throws DynamoException{
		return urlMappingPluginRepository.findUrlMappingPluginsByUrlMappingId(urlMappingId);
	}

	
	@Override
	public PageTemplate savePageTemplate(PageTemplate pageTemplate) throws DynamoException {
		return pageTemplateRepository.save(pageTemplate);
	}

	@Override
	public PageTemplate getPageTemplateById(Long pageTemplateId) throws DynamoException {
		return pageTemplateRepository.findOne(pageTemplateId);
	}

	@Override
	public List<PageTemplate> getPageTemplatesByDomainTemplateId(Long domainTemplateId) throws DynamoException {
		return pageTemplateRepository.findPageTemplatesByDomainTemplateId(domainTemplateId);
	}

}
