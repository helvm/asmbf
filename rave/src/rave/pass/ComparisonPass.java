package rave.pass;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;

import rave.common.ConstructBuilder;
import rave.common.DirectionHelper;
import rave.common.PatternParser;
import rave.common.PatternParser.MatchResult;
import rave.construct.Comparator;
import rave.nodes.INode;

public class ComparisonPass implements IPass {

	public class Tuple<X, Y> {
		public final X x;
		public final Y y;

		public Tuple(X x, Y y) {
			this.x = x;
			this.y = y;
		}
	}
	
	private HashMap<PatternParser, Tuple<Tuple<Integer, Integer>, BinaryOperator<Integer>>> comparsions =
		new HashMap<PatternParser, Tuple<Tuple<Integer, Integer>, BinaryOperator<Integer>>>() {{
			// eq_: 1 -> untouched reg, 4 -> source reg
		    put(new PatternParser("P[P+P-]+P[P-<+P-]P[P+P-]>[P-P[-]]"), new Tuple<>(new Tuple<>(1, 4), (a, b) -> a == b ? 1 : 0));
		}};
	
	public ComparisonPass() {
		comparsions.forEach((a, b) -> a.setPedantic(true));
	}
	
	@Override
	public int match(List<INode> input) {
		Optional<Tuple<Tuple<PatternParser, MatchResult>, Tuple<Tuple<Integer, Integer>, BinaryOperator<Integer>>>> match = comparsions.entrySet().stream().map(
				(a) -> new Tuple<Tuple<PatternParser, MatchResult>, Tuple<Tuple<Integer, Integer>, BinaryOperator<Integer>>>(
						new Tuple<PatternParser, MatchResult>(a.getKey(), a.getKey().tryMatch(input)
				), a.getValue())
		).filter(x -> x.x.y.hasAny).findFirst();

		return match.isPresent() ? match.get().x.y.parsedTerms : 0;
	}

	@Override
	public INode build(List<INode> input) {
		Optional<Tuple<Tuple<PatternParser, MatchResult>, Tuple<Tuple<Integer, Integer>, BinaryOperator<Integer>>>> match = comparsions.entrySet().stream().map(
				(a) -> new Tuple<Tuple<PatternParser, MatchResult>, Tuple<Tuple<Integer, Integer>, BinaryOperator<Integer>>>(
						new Tuple<PatternParser, MatchResult>(a.getKey(), a.getKey().tryMatch(input)
				), a.getValue())
		).filter(x -> x.x.y.hasAny).findFirst();
		
		
		
		return new ConstructBuilder().type(Comparator.class)
				.coefficient(1).setting("comparator", match.get().y.y)
				.setting("delta_1", DirectionHelper.getRelativeDelta(match.get().x.y.variableNodes.get(match.get().y.x.x)))
				.setting("delta_2", DirectionHelper.getRelativeDelta(match.get().x.y.variableNodes.get(match.get().y.x.y)))
				.build();
	}
	
}
