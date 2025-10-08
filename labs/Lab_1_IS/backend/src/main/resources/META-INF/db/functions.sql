DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_language WHERE lanname = 'plpgsql') THEN
    CREATE LANGUAGE plpgsql;
  END IF;
END$$;

CREATE OR REPLACE FUNCTION __pick_column(p_table text, VARIADIC p_candidates text[])
RETURNS text
LANGUAGE plpgsql STABLE
AS $$
DECLARE
  c text;
BEGIN
  FOREACH c IN ARRAY p_candidates LOOP
    IF EXISTS (
      SELECT 1
      FROM information_schema.columns
      WHERE table_schema = 'public'
        AND table_name  = lower(p_table)
        AND lower(column_name) = lower(c)
    ) THEN
      RETURN c;
    END IF;
  END LOOP;
  RAISE EXCEPTION 'None of the candidate columns (%) exist in table %', p_candidates, p_table;
END
$$;


CREATE OR REPLACE FUNCTION vehicle_avg_fuel_consumption()
RETURNS double precision
LANGUAGE plpgsql STABLE
AS $$
DECLARE
  col text;
  sql text;
  res double precision;
BEGIN
  col := __pick_column('vehicle', 'fuel_consumption','fuelConsumption','fuelconsumption');

  sql := format(
    'SELECT avg((%1$I)::double precision) FROM public.vehicle WHERE %1$I IS NOT NULL',
    col
  );

  EXECUTE sql INTO res;
  RETURN res;
END
$$;


CREATE OR REPLACE FUNCTION vehicle_any_with_max_type()
RETURNS public.vehicle
LANGUAGE plpgsql STABLE
AS $$
DECLARE
  col_type text;
  sql      text;
  v        public.vehicle;
BEGIN
  col_type := __pick_column('vehicle','type','vehicletype','vehicle_type');

  sql := format(
    'SELECT * FROM public.vehicle
     WHERE %1$I = (SELECT max(%1$I) FROM public.vehicle)
     LIMIT 1',
    col_type
  );

  EXECUTE sql INTO v;
  RETURN v;
END
$$;


CREATE OR REPLACE FUNCTION vehicle_name_contains(p_sub text)
RETURNS SETOF public.vehicle
LANGUAGE plpgsql STABLE
AS $$
DECLARE
  col_name text;
  sql      text;
BEGIN
  col_name := __pick_column('vehicle','name');

  sql := format(
    'SELECT * FROM public.vehicle WHERE %1$I ILIKE $1',
    col_name
  );

  RETURN QUERY EXECUTE sql USING '%' || p_sub || '%';
END
$$;


CREATE OR REPLACE FUNCTION vehicle_wheels_in_range(p_from bigint, p_to bigint)
RETURNS SETOF public.vehicle
LANGUAGE plpgsql STABLE
AS $$
DECLARE
  col_wheels text;
  sql        text;
BEGIN
  col_wheels := __pick_column('vehicle','number_of_wheels','numberOfWheels','numberofwheels');

  sql := format(
    'SELECT * FROM public.vehicle
     WHERE %1$I IS NOT NULL AND %1$I BETWEEN $1 AND $2',
     col_wheels
  );

  RETURN QUERY EXECUTE sql USING p_from, p_to;
END
$$;


CREATE OR REPLACE FUNCTION vehicle_reset_distance(p_id bigint)
RETURNS void
LANGUAGE plpgsql VOLATILE
AS $$
DECLARE
  col_id   text := __pick_column('vehicle','id');
  col_dist text := __pick_column('vehicle','distance_travelled','distanceTravelled','distancetravelled');
  sql      text;
  epsilon  double precision := 0.000001;
BEGIN
  sql := format(
    'UPDATE public.vehicle SET %1$I = $1 WHERE %2$I = $2',
    col_dist, col_id
  );

  EXECUTE sql USING epsilon, p_id;
END
$$;


