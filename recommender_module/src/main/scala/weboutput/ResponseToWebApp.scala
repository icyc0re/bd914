package weboutput

import scalaj.http.Http
import filtering.MockVectorSimilarity
import vectors.AbstractVector
import utils.Cons

/**
 * Created by Boris on 02/05/2014.
 */
object ResponseToWebApp {
  //Response.replyToWebApp(sorted(0)._1, sorted(0)._2);
  //userId=9792624&venueIds=[4d7a10120ceba1cdf643bb58,518f93e9498eb90b19330131,4b2241def964a520cc4424e3]
  def replyToWebApp(sortedSimilarities: Seq[(String, Seq[(String, Double)])], k: Int, userIndex: Int) = {
    if(userIndex < sortedSimilarities.size && k < sortedSimilarities(userIndex)._2.size){
      Http(Cons.WEBAPP_RESULT_URL)
        .param(Cons.WEBAPP_RESULT_PARAM_USERID, sortedSimilarities(userIndex)._1)
        .param(Cons.WEBAPP_RESULT_PARAM_VENUEIDS, MockVectorSimilarity.getTopKSimilaritiesForUserString(userIndex, sortedSimilarities, k))
        .asString;
    } else {
      throw new Exception("Index out of bounds");
    }
  }
}
