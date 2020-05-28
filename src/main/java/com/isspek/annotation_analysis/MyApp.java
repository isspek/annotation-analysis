package com.isspek.annotation_analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import org.dkpro.statistics.agreement.coding.CodingAnnotationStudy;
import org.dkpro.statistics.agreement.coding.FleissKappaAgreement;
import org.dkpro.statistics.agreement.coding.KrippendorffAlphaAgreement;
import org.dkpro.statistics.agreement.coding.PercentageAgreement;
import org.dkpro.statistics.agreement.distance.NominalDistanceFunction;
import org.dkpro.statistics.agreement.visualization.CoincidenceMatrixPrinter;
import org.dkpro.statistics.agreement.visualization.ReliabilityMatrixPrinter;

public class MyApp {

	public static void main(String[] args) throws IOException {
		int raterCount = Integer.parseInt(args[0]);
		String annotationFileName = args[1];
		String expName = args[2];

		File expFolder = new File("target", expName);
		expFolder.mkdirs();

		CodingAnnotationStudy study = new CodingAnnotationStudy(raterCount);

		BufferedReader reader = new BufferedReader(new FileReader(annotationFileName));
		String line;
		while ((line = reader.readLine()) != null) {
			study.addItemAsArray(line.split("\t"));
		}

		reader.close();

		PrintStream summary = new PrintStream(new File(expFolder, "summary"));

		PercentageAgreement pa = new PercentageAgreement(study);
		summary.println(String.format("Percentage Agreement: %f", pa.calculateAgreement()));

		FleissKappaAgreement kappa = new FleissKappaAgreement(study);
		summary.println(String.format("Fleiss Kappa Agreement: %f", kappa.calculateAgreement()));

		KrippendorffAlphaAgreement alpha = new KrippendorffAlphaAgreement(study, new NominalDistanceFunction());
		summary.println(String.format("Krippendorff Alpha Agreement: %f", alpha.calculateAgreement()));
		summary.println(String.format("Observed Disagreement: %f", alpha.calculateObservedDisagreement()));
		summary.println(String.format("Expected Disagreement: %f", alpha.calculateExpectedDisagreement()));

		CoincidenceMatrixPrinter coincidenceMatrixPrinter = new CoincidenceMatrixPrinter();
		coincidenceMatrixPrinter.print(new PrintStream(new File(expFolder, "coincidence_matrix")), study);

		ReliabilityMatrixPrinter reliabilityMatrixPrinter = new ReliabilityMatrixPrinter();
		reliabilityMatrixPrinter.print(new PrintStream(new File(expFolder, "reliability_matrix")), study);

	}

}
