package org.directwebremoting.spring.namespace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.BeanDefinitionDecorator;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Jose Noheda [jose.noheda@gmail.com]
 */
public class SignatureDecorator implements BeanDefinitionDecorator
{

    private static final Logger log = LoggerFactory.getLogger(SignatureDecorator.class);

    public BeanDefinitionHolder decorate(Node signatureElement, BeanDefinitionHolder bean, ParserContext parserContext)
    {
        BeanDefinitionRegistry registry = parserContext.getRegistry();
        BeanDefinition config = ConfigurationParser.registerConfigurationIfNecessary(registry);

        StringBuffer sigtext = new StringBuffer();
        NodeList children = signatureElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++)
        {
            Node child = children.item(i);
            if ((child.getNodeType() != Node.TEXT_NODE) && (child.getNodeType() != Node.CDATA_SECTION_NODE))
            {
                log.warn("Ignoring illegal node type: " + child.getNodeType());
                continue;
            }
            sigtext.append(child.getNodeValue());
        }

        config.getPropertyValues().addPropertyValue("signatures", sigtext.toString());

        return bean;
    }

}
