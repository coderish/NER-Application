package com.rish.tutorial.ner.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rish.tutorial.ner.model.Type;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

@RestController
@RequestMapping("/api/v1")
public class NERController {

	private StanfordCoreNLP stanfordCoreNLP;

	@Autowired
	public NERController(StanfordCoreNLP stanfordCoreNLP) {
		this.stanfordCoreNLP = stanfordCoreNLP;
	}

	@PostMapping
	@RequestMapping("/ner")
	public Set<String> ner(@RequestBody final String input, @RequestParam final Type type) {
		CoreDocument coreDocument = new CoreDocument(input);
		stanfordCoreNLP.annotate(coreDocument);
		List<CoreLabel> coreLabels = coreDocument.tokens();

		return new HashSet<>(collectList(coreLabels, type));
	}

	private List<String> collectList(List<CoreLabel> coreLabels, final Type type) {
		return coreLabels.stream()
				.filter(coreLabel -> type.getName()
						.equalsIgnoreCase(coreLabel.get(CoreAnnotations.NamedEntityTagAnnotation.class)))
				.map(CoreLabel::originalText).collect(Collectors.toList());
	}
}
